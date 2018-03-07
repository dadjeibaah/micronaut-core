package io.micronaut.inject.configurations

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.context.DefaultApplicationContext
import io.micronaut.context.DefaultBeanContext
import io.micronaut.context.env.PropertySource
import io.micronaut.inject.configurations.requiresbean.RequiresBean
import io.micronaut.inject.configurations.requirescondition2.TrueLambdaBean
import io.micronaut.inject.configurations.requiresconditiontrue.TrueBean
import io.micronaut.inject.configurations.requiresconfig.RequiresConfig
import io.micronaut.inject.configurations.requiresproperty.RequiresProperty
import io.micronaut.inject.configurations.requiressdk.RequiresJava9
import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.context.DefaultApplicationContext
import io.micronaut.context.DefaultBeanContext
import io.micronaut.context.env.MapPropertySource
import io.micronaut.context.env.PropertySource
import io.micronaut.inject.configurations.requiresbean.RequiresBean
import io.micronaut.inject.configurations.requirescondition.TravisBean
import io.micronaut.inject.configurations.requirescondition2.TrueLambdaBean
import io.micronaut.inject.configurations.requiresconditionfalse.TravisBean2
import io.micronaut.inject.configurations.requiresconditiontrue.TrueBean
import io.micronaut.inject.configurations.requiresconfig.RequiresConfig
import io.micronaut.inject.configurations.requiresproperty.RequiresProperty
import io.micronaut.inject.configurations.requiressdk.RequiresJava9
import spock.lang.Ignore
import spock.lang.Specification
/**
 * Created by graemerocher on 19/05/2017.
 */
class RequiresBeanSpec extends Specification {

    void "test that a configuration can require a bean"() {
        given:
        BeanContext context = new DefaultBeanContext()
        context.start()

        expect:
        context.containsBean(ABean)
        context.containsBean(TrueBean)
        context.containsBean(TrueLambdaBean)
        !context.containsBean(RequiresBean)
        !context.containsBean(RequiresConfig)
        !context.containsBean(RequiresJava9)
//        !context.containsBean(TravisBean) // TODO: these are broken because closures are not supported for @Requires( condition = {})
//        !context.containsBean(TravisBean2)
    }

    void "test that a condition can be required for a bean when false"() {
        given:
        BeanContext context = new DefaultBeanContext()
        context.start()

        expect:
        context.containsBean(ABean)
//        !context.containsBean(TravisBean2) // TODO: these are broken because closures are not supported for @Requires( condition = {})
    }

//    @Ignore("it doesn't matter whether TrueEnvCondition returns true or false, context never has TrueBean")
    void "test that a condition can be required for a bean when true"() {
        given:
        BeanContext context = new DefaultBeanContext()
        context.start()

        expect:
        context.containsBean(ABean)
        context.containsBean(TrueBean)
    }

    void "test that a lambda condition can be required for a bean when true"() {
        given:
        BeanContext context = new DefaultBeanContext()
        context.start()

        expect:
        context.containsBean(ABean)
        context.containsBean(TrueLambdaBean)
    }

    void "test requires property when not present"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.start()

        expect:
        !applicationContext.containsBean(RequiresProperty)
    }

    void "test requires property when present"() {
        given:
        ApplicationContext applicationContext = new DefaultApplicationContext("test")
        applicationContext.environment.addPropertySource(PropertySource.of(
                'test',
                ['dataSource.url':'jdbc::blah']
        ))
        applicationContext.start()

        expect:
        applicationContext.containsBean(RequiresProperty)
    }
}