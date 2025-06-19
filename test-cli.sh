#!/bin/bash
EXIT_STATUS=0
mkdir -p demo
./gradlew :test-suite-helloworld-cli:build || EXIT_STATUS=$?
if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi
java -jar test-suite-helloworld-cli/build/libs/test-suite-helloworld-cli-0.1-all.jar create --output demo || EXIT_STATUS=$?
if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi
cd demo
./mvnw test || EXIT_STATUS=$?
if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi
./gradlew test || EXIT_STATUS=$?
if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi
cd ..
rm -rf ../demo
echo "Done!"
