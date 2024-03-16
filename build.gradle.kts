plugins {
	java
	application
}

application {
	mainClass.set("com.styx.shellgames.ShellGamesApplication")
}

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.2"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
	useJUnitPlatform()
}
