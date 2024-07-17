title="Documentation"

multiline markdown prefix << 'JOPA'

**Jopa**  `[ˈʐopə]`  is  a  very  small,  but  powerful  static  website
generator  written in  bash. It  uses a  couple of  conventions to  make
writing stuff fun and easy.

The main idea is to [ab]use env  variables to do both configuration of a
website and also its generation. Everything  in **Jopa** world is a bash
script - including posts you write, layouts and, of course, **`./jopa`**
itself.

JOPA

multiline indexer_main << 'JOPA'
  <section class="index" id="jopa-${target}">
    <div class="index-header">
      <a class="separate-link" href="${target}">(read on a separate page)</a></span>
      <h3><a class="self-link" href="#jopa-${target}">${title}</a></h3>
    </div>
    ${content}
  </section>
JOPA
index_main="$(each "$pages" render "$indexer_main")"

index_toc="$(each "$pages" render '<li><a href="#jopa-${target}">${title}</a></li>')"

multiline content << JOPA
  <section>$prefix</section>
  <ol class="toc">$index_toc</ol>
  $index_main
JOPA
