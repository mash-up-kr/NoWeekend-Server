package noweekend.mcpserver.config

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.jasypt.iv.NoIvGenerator
import org.jasypt.salt.RandomSaltGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableEncryptableProperties
@Configuration
class JasyptConfig {
    @Value("\${JASYPT_ENCRYPTOR_PASSWORD}")
    private val password: String? = null

    /**
     * Creates and configures a Jasypt string encryptor bean for encrypting and decrypting properties.
     *
     * The encryptor uses the password from the environment variable `JASYPT_ENCRYPTOR_PASSWORD`, the `PBEWithMD5AndDES` algorithm, and base64 output encoding.
     *
     * @return A configured `StringEncryptor` instance for property encryption.
     */
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()

        config.password = password
        config.algorithm = "PBEWithMD5AndDES"
        config.keyObtentionIterations = 1000
        config.poolSize = 1
        config.providerName = "SunJCE"
        config.saltGenerator = RandomSaltGenerator()
        config.ivGenerator = NoIvGenerator()
        config.stringOutputType = "base64"

        encryptor.setConfig(config)
        return encryptor
    }
}
