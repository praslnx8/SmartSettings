object ApplicationConfig {

    val mongoDbHost = environment("MONGODB_HOST", "127.0.0.1")
    val mongoDbPort = environment("MONGODB_PORT", "27017")
    val mongoDbUserName = environment("MONGODB_USERNAME", "user")
    val mongoDbPassword = environment("MONGODB_PASSWORD", "secret")

    private fun environment(name: String, default: String): String {
        val value = System.getenv(name)
        print("ENV : $value")
        return value ?: default
    }
}