if [[ $(apk list --installed | grep git) == "" ]]; then
  apk add git;
fi