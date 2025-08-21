def cmd(String cmdStr) {
	if (isUnix()) {
		sh cmdStr
	} else {
		powershell cmdStr
	}
}

final SUITES_REPO = "git@github.com:LunarG/ci-gfxr-suites.git"
final TESTS_REPO = "git@github.com:LunarG/VulkanTests.git"

def call(Map params) {
    call(params.platform, params.type, params.bits, params.device)
}

def call(String platform, String type, String bits, String device) {
    gitCheckout(TESTS_REPO, 'master')
    unstash name: getStashName(platform, type, bits)

    cmd "python3 VulkanTests/gfxrecontest.py --build-mode ${type} --bits ${bits} ${args} --suite \"ci-gfxr-suites/${GFXRECON_TRACE_SUBDIR}/${TEST_SUITE}\" --trace-dir \"${GFXRECON_TRACE_DIR}\""
}
