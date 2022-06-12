package de.progeek.tasks.config

import com.fasterxml.classmate.TypeResolver
import kotlinx.coroutines.flow.Flow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import springfox.documentation.builders.*
import springfox.documentation.schema.AlternateTypeRules
import springfox.documentation.schema.WildcardType
import springfox.documentation.service.BasicAuth
import springfox.documentation.service.HttpAuthenticationScheme
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket


@Configuration
class OpenApiConfiguration(val typeResolver: TypeResolver) {
    val BASIC_AUTH_NAME = "basic"

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
            .securitySchemes(arrayListOf(basicAuth()))
            .securityContexts(arrayListOf(basicSecurityContext()))
            .select()
            .paths(PathSelectors.any())
            .build()

    }


    private fun basicSecurityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(listOf(SecurityReference(BASIC_AUTH_NAME, arrayOf())))
            .forPaths(PathSelectors.ant("/task/**"))
            .build()
    }

    private fun basicAuth(): SecurityScheme = HttpAuthenticationScheme.BASIC_AUTH_BUILDER
        .name(BASIC_AUTH_NAME)
        .description("Basic authentication")
        .build()


    private fun apiInfo() = ApiInfoBuilder()
        .title("Tasks Management API")
        .version("0.1.0")
        .build()

}
