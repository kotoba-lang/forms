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

`read-form-envelope` gives the payload back as plain JSON — string keys,
`"text"` where `:text` went in — because that is what the current `transit`
wire carries. `rehydrate-form` turns it back into a form, and
`form-of-envelope` does both in one step.

**Anything that validates has to rehydrate first.** `forms.validate` reads
`:forms/fields`, which on a projected payload is `nil`, so it iterates
nothing and reports no problems — a broken form comes back "valid" rather
than coming back wrong. There is a test for exactly that.

## Test

```bash
clojure -M:test
```
