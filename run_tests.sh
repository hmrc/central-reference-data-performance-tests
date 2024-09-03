#!/bin/sh

smoke_tests=true
run_local=true

while getopts fsh flags
do
  case "${flags}" in
    f)
      echo "run full tests"
      smoke_tests=false
      ;;
    s)
      echo "run tests on staging"
      run_local=false
      ;;
    h)
      echo "usage run_tests.sh [-ls]"
      exit 0
      ;;
    ?)
      echo "Invalid option: -S{OPTARG}."
      exit 1
      ;;
  esac
done

sbt -Dperftest.runSmokeTest=$run_local -DrunLocal=$smoke_tests  "Gatling / test"
