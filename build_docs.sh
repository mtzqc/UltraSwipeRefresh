#!/bin/bash

set -ex

# Generate the API docs (Dokka v2: aggregation task + output dir changed)
./gradlew dokkaGeneratePublicationHtml

mkdir -p docs/api
mv build/dokka/html/* docs/api

# Copy in special files that GitHub wants in the project root.
GITHUB_URL=https://github.com/mtzqc/UltraSwipeRefresh/
echo $GITHUB_URL
sed "/<!-- end -->/q" README.md > docs/index.md
# sed -i "s|app/src/main/ic_launcher-web.png|ic_logo.png|g" docs/index.md
sed -i "s|](composeApp|](${GITHUB_URL}blob/master/composeApp|g" docs/index.md
sed -i "s|](refresh|](${GITHUB_URL}blob/master/refresh|g" docs/index.md
cat CHANGELOG.md | grep -v '## 版本日志' > docs/changelog.md

cp -r  art docs/art
# cp app/src/main/ic_launcher-web.png docs/ic_logo.png

# Build the site locally
mkdocs build
