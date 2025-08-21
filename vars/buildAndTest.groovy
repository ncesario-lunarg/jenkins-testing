
def cmd(String cmdStr) {
	if (isUnix()) {
		sh cmdStr
	} else {
		powershell cmdStr
	}
}

final GFXR_REPO = "git@github.com:LunarG/gfxreconstruct.git"
final SUITES_REPO = "git@github.com:LunarG/ci-gfxr-suites.git"
final TESTS_REPO = "git@github.com:LunarG/VulkanTests.git"

def gitCheckout(String url, String branch) {
   checkout scmGit(
      branches: [[name: branch]],
      userRemoteConfigs: [[url: url]],
      extensions: [
            cloneOption(noTags: true),
            cloneOption(shallow: true),
			submodule(depth: 1, recursiveSubmodules: true)
      ]
   )
}

def getStashName(String platform, String type, String bits) {
  return "${platform}-build-${type}-${bits}-artifacts"
}

def call(Map params) {
    call(params.platform, params.type, params.bits, params.device)
}

def call(String platform, String type, String bits, String device) {
    stage('build') {
        agent {
            label "${platform}-build"
        }
        steps {
            // The fetch/checkout lets it work with Gerrit
            cmd 'submodule update --init --recursive'
            cmd 'git describe --tags --always'

            gitCheckout(SUITES_REPO, 'master')
            gitCheckout(TESTS_REPO, 'master')

            //cmd "python3 VulkanTests/gfxrecontest.py --build-mode ${type} --bits ${bits} ${args} --suite \"ci-gfxr-suites/${GFXRECON_TRACE_SUBDIR}/${TEST_SUITE}\" --trace-dir \"${GFXRECON_TRACE_DIR}\""
            cmd "python3 VulkanTests/gfxrecontest.py --build-mode ${type} --bits ${bits} ${args}"

            // TODO: only stash necessary binaries, not the entire build directory
            stash includes: "build/layer/**/*", "build/tools/**/*", name: getStashName(platform, type, bits)
        }
    }

    stage('test') {
        agent {
            label "${platform}-test && ${platform}-${device}"
        }

        steps {
            gitCheckout(TESTS_REPO, 'master')
            unstash name: getStashName(platform, type, bits)

            cmd "python3 VulkanTests/gfxrecontest.py --build-mode ${type} --bits ${bits} ${args} --suite \"ci-gfxr-suites/${GFXRECON_TRACE_SUBDIR}/${TEST_SUITE}\" --trace-dir \"${GFXRECON_TRACE_DIR}\""
        }
    }
}
