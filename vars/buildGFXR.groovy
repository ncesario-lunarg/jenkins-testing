import static Constants.*

class Constants {
    static final SUITES_REPO = "git@github.com:LunarG/ci-gfxr-suites.git"
    static final TESTS_REPO = "git@github.com:LunarG/VulkanTests.git"
}

def sep() {
    return isUnix() ? '/' : '\\'
}

def envSep() {
    return isUnix() ? ':' : ';'
}

def cmd(String cmdStr) {
	if (isUnix()) {
		sh cmdStr
	} else {
		powershell cmdStr
	}
}

def gitCheckout(String url, String branch, String d) {
   dir (d) {
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
}

def getStashName(String platform, String type, String bits) {
  return "${platform}-build-${type}-${bits}-artifacts"
}

def pathJoin(String[] path_comps) {
    return path_comps.join(sep())
}

def envJoin(String[] env_comps) {
    return env_comps.join(envSep())
}

def call(Map params) {
    call(params.platform, params.type, params.bits, params.device)
}

def call(String platform, String type, String bits, String device) {
    // The fetch/checkout lets it work with Gerrit
    cmd 'git describe --tags --always'

    gitCheckout(SUITES_REPO, 'master', 'ci-gfxr-suites')
    gitCheckout(TESTS_REPO, 'ncesario-python-requirements', 'VulkanTests')

    dir ('VulkanTests') {
        cmd "python3 -m venv .venv"
        env.VIRTUAL_ENV = pathJoin(pwd(), '.venv')
        env.PATH = envJoin(pathJoin(env.VIRTUAL_ENV, 'bin'), env.PATH)
        cmd "pip install -r requirements.txt"
    }

    unstash name: getStashName(platform, type, bits)

    //cmd "python3 VulkanTests/gfxrecontest.py --build-mode ${type} --bits ${bits} ${args} --suite \"ci-gfxr-suites/${GFXRECON_TRACE_SUBDIR}/${TEST_SUITE}\" --trace-dir \"${GFXRECON_TRACE_DIR}\""
    cmd "python3 VulkanTests/gfxrecontest.py --os AndroidTestOS --build-mode ${type.capitalize()} --bits ${bits}"
}
