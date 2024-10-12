# Changelog (TogaGem)
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


## [Unreleased]

### Fixed
- Update Jacoco & SoftBugs for JDK23
- Update Maven plugins
- Update PMD & Checkstyle
- Update GitHub workflows


## [3.122.4] - 2023-10-18

### Changed
- ホスティングをOSDNからGitHubへ移行。
- JUnit4からJUnit5へ移行

### Fixed
- Maven plugins のJDK20対応


## [3.122.2] - 2019-07-02

### Secure
- Prevent XXE vulnerabilities with XML-schema(XSD).
- Remove discarded XML classes.


## [3.121.2] - 2019-06-06

### Changed
- DatatypeIo is public now, for replacing JAXB.


## [3.120.2] - 2019-06-04

### Changed
- replacing JAXB DatatypeConverter.


## [3.101.106] - 2019-06-02

### Changed
- Mercurial(3.102.4)からGit(3.101.105-SNAPSHOT)へSCMを移行。

### Build
- 必須環境をJavaSE8に引き上げ。


## → CHANGELOG.old (2009-2013) へ続く…


[Unreleased]: https://github.com/olyutorskii/TogaGem/compare/release-3.122.2...HEAD
[3.122.2]: https://github.com/olyutorskii/TogaGem/compare/release-3.121.2...release-3.122.2
[3.121.2]: https://github.com/olyutorskii/TogaGem/compare/release-3.120.2...release-3.121.2
[3.120.2]: https://github.com/olyutorskii/TogaGem/compare/release-3.101.106...release-3.120.2
[3.101.106]: https://github.com/olyutorskii/TogaGem/releases/tag/release-3.101.106


--- EOF ---
