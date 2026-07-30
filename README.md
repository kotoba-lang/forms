# forms

[![CI](https://github.com/kotoba-lang/forms/actions/workflows/ci.yml/badge.svg)](https://github.com/kotoba-lang/forms/actions/workflows/ci.yml)

Portable CLJC model for kotoba-lang/forms.

Pages editor: https://kotoba-lang.github.io/forms/

The Pages UI is local to kotoba-lang and does not redirect to external hosts.

## Wire

`forms.wire/form-envelope` wraps a form in the shared Kotoba office envelope,
using `application/json` and the `:forms/form` resource kind — the same
arrangement `sheets` and `docs` have. Forms was the one without it, because
`transit.core/office-resource-kinds` is closed and did not list `:forms/form`
until now.

The read side gives the payload back as plain JSON — string keys, vectors —
rather than the EDN that went in. That is the current `transit` contract.
`sheets` and `docs` assert lossless round-trips, and can, only because they
pin the last commit before the wire moved off Transit-tagged JSON; this
library pins the current one because that is where `:forms/form` exists.

## Test

```bash
clojure -M:test
```
