title="Code"

# prepend code with four spaces to make markdown happy
code="$(cat ./jopa | awk '{print "    " $0}')"
ln="$(cat ./jopa | wc -l)"

multiline markdown content << JOPA

Here is the **full** \`jopa\` code, just $ln lines:

$code

![](https://github.com/neoascetic/jopa/assets/725836/93599ed6-8f6b-4449-a042-bfccd12ff62a)
*Other site generators looking at jopa's source code*

JOPA
