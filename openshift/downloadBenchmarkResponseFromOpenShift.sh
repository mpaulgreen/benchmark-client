#!/bin/sh

echo " Assuming you are on the RedHat VPN and currently authorized with open shift cluster...!!"


if [ -d "pod-downloads" ]; then rm -Rf pod-downloads; fi

mkdir pod-downloads
cd pod-downloads

echo "Downloading the files from open shift cluster. This may take some time depending on the size of the files.."
# 1 - capture the pod logs from open shift to local folder
(for pod in $(oc get pods -l app=benchmark-client -o custom-columns=POD:.metadata.name --no-headers); do echo $pod; oc rsync $pod:/tmp/database-message.log . && mv database-message.log $pod.log; done;)

echo "Successfully downloaded files from Open Shift cluster"

