#!/usr/bin/env just --justfile


check:
    #!/usr/bin/env bash
    set -euxo pipefail
    cd stellar
    sbt scalafmtCheckAll
    sbt 'scalafixAll --check'

fix:
    #!/usr/bin/env bash
    set -euxo pipefail
    cd stellar
    sbt scalafmtAll
    sbt scalafixAll

test:
    #!/usr/bin/env bash
    set -euxo pipefail
    cd stellar
    sbt test
