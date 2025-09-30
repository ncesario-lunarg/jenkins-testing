def call(Map params) {
	return call(params.platform, params.type, params.bits)
}

def call(String platform, String type, String bits) {
  return "${platform}-build-${type}-${bits}-artifacts"
}
