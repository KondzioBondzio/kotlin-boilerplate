# kotlin-boilerplate

## How to run

* Make sure you have proper java version (21) - check `languageVersion` property in `build.gradle.kts` file.
* Make sure your docker is running
* run docker compose -f docker-compose.local.yml build
* run docker compose -f docker-compose.local.yml up -d
* optionally edit AppRunner start it locally app will be connected to docker containers

#### execute tests & build app
`./gradlew clean build`

#### db migration
Hibernate is set via property `ddl-auto: update`. In such case it will create new tables and modify old ones, however it's not recommended.

#### swagger
* UI path - ` /swagger-ui`
* json path - `/api-docs`


## Best practise
### 1. code
1. follow ports&adapters architecture
2. add logs near IO operations - may be before and after, log some id or even entire body
3. create own exceptions and catch external ones
4. use properties files for magic values

### 2. REST & message
1. separate message body (dto) from entities
2. split controllers by it's unit 
3. avoid logic in controllers

### 3. MONGO & DB
1. avoid excessive embedding
2. avoid large-scale cascading updates
3. create indexes
4. avoid logic in repositories

### 4. external clients
1. avoid logic in clients
2. separate dto from business model - never store it directly in DB

### 5. testing
1. create self describing test
2. test name should have case description in their names, preferably `should ... when ...` ie. `should reject payment when account balance too low`
3. test method should be small, extract code to separate methods, interfaces, ie.

```kotlin
    @Test
    fun `should create sample`() {
        // given
        val request = SampleRequest( // in case of complex object you can have method "aSample(..)" which creates an object
            name = "Sample Name",
            list = listOf(
                NestedObjectDto(
                    10, "nested-1"
                )
            )
        )

        // when
        val sampleId = createSample(request).body!!.id

        // then
        val response = fetchSample(sampleId)
        assertTrue(response.statusCode.is2xxSuccessful)
        assertSampleHasData(response.body, request)
    }

    @Test
    fun `should fetch data from external service`() {
        // given
        val responseMessage = "Just a message"
        stubExternalServiceResponse(responseMessage)

        // when
        val response = fetchExternalData()

        // then
        assertTrue(response.statusCode.is2xxSuccessful)
        assertThat(response.body!!.message).isEqualTo(responseMessage)
    }
```