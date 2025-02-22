title="Quickstart"

multiline markdown content << 'JOPA'

To start using Jopa, you first  need to make sure you have prerequisites
installed. Chances are you already  have everything on your system. Jopa
uses  `bash >=  3`  and [`envsubst`][envsubst]  from the  `gettext-base`
package. If these are not installed, go do, I'll wait.

Okay, now, when dependencies are ready, you need to grab the actual Jopa
code.  You  can  grab  the  code  [directly  from  GitHub][gh]  or  just
copy-paste it  from the [code](code.html) page.  Put the code in  a file
named `jopa` and do not forget to make it executable:

    chmod +x ./jopa

Now, you need to create `layout.jsh` file, where the base layout for all
your pages will be defined. Something  like below, I'll use the simplest
possible HTML5 page there and you can extend it later.

    cat <<'EOF' > layout.jsh
    
    website_title="My wonderful website"
    
    multiline layout << 'JOPA'
    <!doctype html>
    <meta charset=utf-8>
    <title>${title} | ${website_title}</title>
    ${content}
    JOPA
    
    EOF

Okay, let's  jump to the  fun stuff and create  the first page  of your.
future website  The page will be  located in `pages/` directory,  so we.
need to create one beforehand                                          .

    mkdir -p pages

    cat <<'EOF' > pages/index.jsh
    
    title="My first page"
    
    multiline content << 'JOPA'
    Hello world!
    <br>
    I am a happy Jopa user.
    JOPA

    EOF

Okay, we all set, let's just ask Jopa to do its work:

    ./jopa

Done! Jopa  created your page  in `www` folder, you  can now open  it in
your browser:

    open www/index.html

Congratulations!  You are  now an  official  Jopa blogger.  You can  now
create  more pages,  edit example  layout  template, or  jump into  more
advanced topics.

[envsubst]: https://www.gnu.org/software/gettext/manual/html_node/envsubst-Invocation.html
[gh]: https://github.com/neoascetic/jopa

JOPA
