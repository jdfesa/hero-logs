#!/usr/bin/env bash

set -euo pipefail

readonly SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
readonly REPO_ROOT="$(cd -- "${SCRIPT_DIR}/.." && pwd)"
readonly MODE="${1:-quick}"
readonly GRADLE_WORKERS="${HEROLOGS_GRADLE_WORKERS:-2}"

cd "${REPO_ROOT}"

verify_docs() {
    git diff --check

    local required_file
    for required_file in \
        AGENTS.md \
        docs/agent/PROJECT_STATE.md \
        docs/agent/CURRENT_PHASE.md \
        docs/agent/PHASE_PLAN.md; do
        if [[ ! -s "${required_file}" ]]; then
            echo "Missing required agent file: ${required_file}" >&2
            return 1
        fi
    done
}

run_unit_tests() {
    ./gradlew \
        --no-daemon \
        --max-workers="${GRADLE_WORKERS}" \
        :app:testDebugUnitTest
}

run_debug_build() {
    ./gradlew \
        --no-daemon \
        --max-workers="${GRADLE_WORKERS}" \
        :app:assembleDebug
}

run_debug_android_test_build() {
    ./gradlew \
        --no-daemon \
        --max-workers="${GRADLE_WORKERS}" \
        :app:assembleDebugAndroidTest
}

case "${MODE}" in
    docs)
        verify_docs
        ;;
    quick)
        verify_docs
        run_unit_tests
        ;;
    full)
        verify_docs
        # Separate invocations reduce Gradle daemon memory pressure on local Macs.
        run_unit_tests
        run_debug_build
        run_debug_android_test_build
        ;;
    *)
        echo "Usage: $0 [docs|quick|full]" >&2
        exit 2
        ;;
esac
