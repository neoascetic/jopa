title="Extending Jopa"
description="Discover the power of Jopa and learn how to customize it for your needs"

multiline markdown content << 'JOPA'

Jopa's  true  power  lies  not  just  in  its  simplicity,  but  in  its
extensibility.  Since  everything  in  Jopa is  a  bash  script,  you're
essentially in  control of every  aspect of site generation.  This means
you can modify,  extend, or completely transform Jopa to  fit your needs
without  touching  the  core  code.  Want to  use  Markdown  instead  of
bash  scripts? No  problem. Need  to  add custom  processing? Easy.  The
possibilities are endless.

In this  article, we'll explore  how to extend  Jopa to work  with plain
Markdown  files while  keeping  all  the power  and  flexibility of  the
original approach. We'll see how a  few lines of code can transform Jopa
into a Markdown-powered  static site generator that still  uses the same
layout  system  and generation  logic.  This  is  the beauty  of  Jopa's
design  - you  can change  the input  format without  changing the  core
architecture.

#### Creating a Markdown Extension

To  extend Jopa  for Markdown  support, we'll  create a  separate script
called `extended-jopa`. This approach  keeps the original Jopa untouched
while giving us full control over the extension.

The basic  structure is simple  - we source  the original Jopa  and then
override the functions we need:

    #!/usr/bin/env bash

    # Override pages to look for .md files instead of .jsh
    export pages="pages/*.md"

    # Source the original Jopa
    source jopa

    # Now we can override functions as needed...

    # Call the main function
    jopa

#### Adding Markdown Processing

Now let's add  the actual Markdown processing functionality.  We need to
override the `read_page()` function to handle `.md` files differently:

##### Overriding read_page()

The `read_page()` function is the  heart of Jopa's page processing. It's
responsible for reading a file  and setting up the environment variables
that will be used during rendering.  In the original Jopa, this function
sources each  `.jsh` file  as a bash  script, which  populates variables
like `title`, `content`, and `description`.

By overriding this function, we can  change how different file types are
processed. Our version  checks if the file is a  Markdown file (based on
its extension) and handles it differently from regular `.jsh` files - by
calling `slurp_markdown` function, which we will cover next.

    # Override read_page to handle Markdown files
    read_page() {
      set -a
      src="${1##*/}"
      target="${src%.*}$ext"
      if [[ "${src##*.}" == "md" ]]; then
        slurp_markdown $1
      else
        source "$1" >&2
      fi
    }

##### Creating slurp_markdown()

The `slurp_markdown()`  function is where  the real magic  happens. This
function  takes  a  Markdown  file  and  transforms  it  into  the  same
environment  variables  that  Jopa  expects  -  `title`,  `description`,
`content`, etc.

The function works in two phases. First, it parses the YAML front matter
at the  top of the  file (the  metadata section between  `---` markers).
This  is where  you  define variables  like  `title` and  `description`.
Second, it takes the remaining Markdown  content and converts it to HTML
using the `redcarpet` tool.

**Note:**  Before  using this  extension,  you'll  need to  install  the
`redcarpet` Ruby gem. On Ubuntu/Debian systems, you can install it with:

    sudo apt install ruby-redcarpet

The parsing uses  a simple state machine with three  modes. Mode 1 looks
for the start of  YAML front matter, Mode 2 reads  the YAML metadata and
converts it to  bash variables, and Mode 3 collects  the actual Markdown
content. Think of it  as a very patient robot reading  your file line by
line, trying to figure out what you want.

    # Function to parse Markdown with YAML front matter
    slurp_markdown() {
      local mode=1  # Start in mode 1: looking for first ---

      while IFS= read p; do
        if [[ $mode -eq 3 ]]; then
          # Mode 3: collecting Markdown content
          printf -v content '%s\n%s' "$content" "$p"
        elif [[ $mode -eq 2 && $p != "---" ]]; then
          # Mode 2: parsing YAML metadata (skip the closing ---)
          read var val <<< "$p"
          printf -v "${var%:}" '%s' "$val"  # Remove colon and set variable
        elif [[ $p == "---" ]]; then
          # Found a --- marker, move to next mode
          ((mode++))
        else
          # No YAML front matter found
          echo "No YAML front matter detected"
          exit 1
        fi
      done < $1

      # Convert Markdown content to HTML using redcarpet
      printf -v content '%s' "$(redcarpet --smarty --parse-fenced-code-blocks <<< "$content")"
    }

##### Complete extended-jopa Script

Here's the complete `extended-jopa` script that combines everything:

    #!/usr/bin/env bash

    export pages="pages/*.md"

    source jopa

    read_page() {
      set -a
      src="${1##*/}"
      target="${src%.*}$ext"
      if [[ "${src##*.}" == "md" ]]; then
        slurp_markdown $1
      else
        source "$1" >&2
      fi
    }

    slurp_markdown() {
      local mode=1
      while IFS= read p; do
        if [[ $mode -eq 3 ]]; then
          printf -v content '%s\n%s' "$content" "$p"
        elif [[ $mode -eq 2 && $p != "---" ]]; then
          read var val <<< "$p"
          printf -v "${var%:}" '%s' "$val"
        elif [[ $p == "---" ]]; then
          ((mode++))
        else
          echo "No YAML front matter detected"
          exit 1
        fi
      done < $1
      printf -v content '%s' "$(redcarpet --smarty --parse-fenced-code-blocks <<< "$content")"
    }

    jopa

#### Using the Extension

Now that  we have our `extended-jopa`  script, let's see how  to use it.
First, save the script as `extended-jopa` and make it executable:

    chmod +x extended-jopa

Create  a Markdown  file  in  your `pages/`  directory  with YAML  front
matter:

    ---
    title: My First Markdown Page
    description: This is a test page written in Markdown
    ---

    # Welcome to My Site

    This is a **Markdown** page that will be processed by our Jopa extension.

    ## Features

    - Supports GitHub Flavored Markdown
    - YAML front matter for metadata
    - Same layout system as regular Jopa
    - Code blocks with syntax highlighting

    ```bash
    echo "Hello, Jopa!"
    ```

Then run the extension:

    ./extended-jopa

The script will  process all `.md` files in your  `pages/` directory and
generate HTML files in the `www/` directory, just like the original Jopa
but  with Markdown  support.  It's  like giving  Jopa  a new  superpower
without changing its personality.

#### The Power of Extension

This example demonstrates the true power of Jopa's design. By creating a
simple wrapper  script, we've transformed Jopa  from a bash-script-based
generator  into a  Markdown-powered static  site generator,  all without
modifying the  core code.  The same layout  system, the  same generation
logic, the same flexibility - just a different input format.

You can  apply this  same principle  to extend  Jopa in  countless other
ways. Want  to support  AsciiDoc? Just add  AsciiDoc processing  to your
`extended-jopa` script.  Need to  process JSON  files? Add  JSON parsing
functions to the same script. The possibilities are limited only by your
imagination and bash scripting skills.

This is the beauty  of Jopa's simplicity - it's not just  a tool, it's a
framework for building your own static site generators.

Have fun!

JOPA
