package de.progeek.tasks.config

import com.fasterxml.classmate.TypeResolver
import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import springfox.documentation.builders.*
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.schema.WildcardType
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class OpenApiConfiguration(val typeResolver: TypeResolver) {
    @Bean
    fun treenetalerApi(): Docket {

        val globalResponses = listOf(
            ResponseBuilder().code("401").description("Unauthorized").build(),
            ResponseBuilder().code("403").description("Forbidden").build(),
            ResponseBuilder().code("500").description("Internal Server Error").build()
        )

        return Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .globalResponses(HttpMethod.GET, globalResponses)
            .globalResponses(HttpMethod.POST, globalResponses)
            .globalResponses(HttpMethod.DELETE, globalResponses)
//            .ignoredParameterTypes(Principal::class.java, UsernamePasswordAuthenticationToken::class.java, AbstractAuthenticationToken::class.java)
            .alternateTypeRules(
                AlternateTypeRules.newRule(
                    typeResolver.resolve(Flow::class.java, WildcardType::class.java),
                    typeResolver.resolve(List::class.java, WildcardType::class.java)
                )
            )
            .select()
            .paths(PathSelectors.any())
            .build()
    }

    private fun apiInfo() = ApiInfoBuilder()
        .title("Tasks Management API")
        .version("0.1.0")
        .build()

}
