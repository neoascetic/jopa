title="Internals"
description="Find out what happens under Jopa's hood"

multiline markdown content << 'JOPA'

The idea of  Jopa is  extendability through  simplicity. Everything  in
Jopa's  world is  a bash  script,  and almost  everything is  controlled
through the use of environment variables.

This is done for you to be in control of what is going on and be able to
apply any logic at any step of site generation. You can do anything bash
scripts  can,  and  modify your  content  as  you  wish  - no  need  for
unnecessary plugins, libs, yadda-yadda-yadda. Just  you and your shell -
`grep`, `cat` and `cut`, anything.

But how does Jopa actually work?

#### Pages

First, it  goes  through  the  list of  pages,  specified  in  `$pages`
variable, and, to render them, does `source` them as a bash script. This
leads the environment for further processing.  The sourcing is done on a
per-page scope, to avoid interference.

By default, `$pages` equals to  `pages/*.jsh` wildcard. The extension is
short for "Jopa shell", to differ  your posts from regular shell scripts
(remember, everything is a script).

Pages  can have  any arbitrary  logic, but  most of  the time  they just
define metadata  such as title,  description and, obviously,  the actual
page content.  To make  text easier to  write, Jopa  defines `multiline`
helper with an  optional "filter" that can be used  to transform content
from one format  to another (we are generating a  website, after all, so
we probably  want the content to  be in HTML).  Here is an example  of a
page:

    title="My wonderful page"
    
    multiline markdown content << 'JOPA'
    
    # Hello World!
    
    Just my blog post, how are you?
    
    JOPA

Here, we  define two env  variables: `$title` and `$content`  (names are
arbitrary),  but  `$content` is  defined  with  `multiline` helper  that
passes  text  through  `markdown`  utility  (which  you  should  install
independently) to generate HTML content.  Notice `<< 'JOPA'` on the line
with `multiline`  call and `JOPA` at  the very end. These  are so-called
"heredocs" and in  bash to define multiline strings. You  can read about
them [somewhere  else][heredoc], just  note, that most  of the  time you
want the first  delimiting identifier to be in  single quotes, otherwise
your text is subject of command evaluation which is a dangerous thing...
Imagine the following page:

    multiline content << JOPA

    Don't do $(rm -rf ~/) in your scripts!

    JOPA

You probably  don't want this to  be executed... But sometimes  you **do
want**  the  evaluation  in  these strings  (for  example,  for  in-line
variable expansion), okay, it's up to you. Just be warned.

Another note  is that you  can use *any*  thing as your  delimiter, even
Emojis. Isn't this fun?

#### Layouts

Next, after  a page  is sourced  env variables  are (re)defined,  but if
`$layout` is not defined yet while `$layout_file` is, the latter is also
sourced. The script  in this file **must** define `$layout`  (and can do
this conditionally).  Again, you can  have any logic, for  example, have
defaults for `$title`:

    website_title="My Website"

    # prepend website title to any page's title
    if [[ "$title" ]]; then
      head_title="$title | $website_title"
    else
      head_title="$website_title"
    fi

    # define mandatory layout variable
    multiline layout << 'JOPA'

    <!doctype html>
    <meta charset=utf-8>
    <title>${head_title}</title>
    <h1>${title}</h1>
    ${content}

    JOPA

Then,  the string  in `$layout`  is  used as  a template  to render  the
resulting file  using `envsubst`  utility, which basically  replaces all
variables in the template with their actual values. Logic-less templates
you say? Kind of.

Well, is that simple.

#### Indexes

But  here  is one  more  thing.  What about  the  index  page, table  of
contents, Atom/RSS feeds and all alike?  We need to display links to our
wonderful pages somehow, in most cases  on the main page - the website's
index. For this purpose, there are *indexes* and the `each` helper.

*Indexes* are regular  pages, except that they  are processed separately
from other  pages and, obviously, are  not "indexed", i. e.  will not be
present in  the resulting  list of  pages. You  must specify  indexes in
space-separated `$indexes` env variable, for example:

    # add Table Of Contents page to the list of indexes
    indexes="$indexes $from/toc.jsh"

By default, only `$from/index.jsh` is in this list.

Below is an example of such a page:

    multiline indexer_main << 'JOPA'
      <li>
        <a href="/${target}">${title}</a>
      </li>
    JOPA
    index_main="$(each "$pages" render "$indexer_main")"

    multiline content << JOPA
      My posts:
      <ol>${index_main}</ol>
    JOPA

Here, we  have regular page  content defined, but it  uses `$index_main`
variable, which  is defined as the  result of `each` function  call on a
set of `$pages`, and each page is then fed into `render "$indexer_main"`
function, i.  e. rendered with the  defined template. You also  see that
`$target`  variable is  used -  this is  a file  name for  a page  being
processed and you can use it to refer to the page.

Of course,  instead of calling `render`  you can call your  own function
which can do filtering, skip some  pages you don't want to be displayed,
and only then call `render` from.

Hope this  sheds some light on  how this pretty simple  thing is working
for you to be able to move  to the next (but optional!) step - extending
Jopa.



[heredoc]: https://en.wikipedia.org/wiki/Here_document

JOPA
