def call(String cmdStr) {
	if (isUnix()) {
		sh cmdStr
	} else {
		powershell cmdStr
	}
}
