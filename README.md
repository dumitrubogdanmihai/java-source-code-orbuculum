# java-source-code-orbuculum

Java source code search engine that allow to execute complex queries against indexed files.

## Status
Proof of concept

## Architecture (big picture)
Is composed of:
* 2 Docker containers:
    - "orbuculum"
    - "indexer-agent"
* 2 Eclipse plugins that connect with above mentioned containers

## Prerequisites
* Maven
* Docker

## Installation:
* mvn install

## Usage
* Start both containers 'orbuculum' and 'indexer-agent'.
* Install the plug-in via the local update-site generated by 'mvn install' that can be found in './eclipse-plugin/updatesite/target' or run both plug-in projects directly from Eclipse via 'Run As/Eclipse Application'.
* Open Eclipse and index projects via 'Indexer' view [0] and search via 'Semantic Search' tab from 'Search' dialog.
[0] Note that it may take a while to index a repository direclty from GitHub so don't panic right away.
## License

This project is licensed under the MIT License \([LICENSE](LICENSE)\).
