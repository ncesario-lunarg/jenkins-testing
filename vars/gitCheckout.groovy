def call(String url, String branch, String d) {
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

def call(Map params) {
	call(params.url, params.branch, params.d)
}
