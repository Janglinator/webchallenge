package _old

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan()
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}