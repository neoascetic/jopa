website_name="JOPA"
website_description="Simple thus hackable static site generator"
description=${description:-$website_description}

multiline layout << 'JOPA'
<!doctype html>
<html lang="en">

<head>
  <meta charset="utf-8">
  <title>${website_name} ${title}</title>
  <meta name="viewport" content="width=device-width">
  <meta name="description" content="${description}">
  <link rel="stylesheet" href="/assets/styles.css">
</head>

<body>
  <header>
    <h1><a href="/">${website_name}</a></h1>
    <h2>${website_description}</h2>
  </header>

  <div class="main">
    <article>
     <h2>${title}</h2>
     ${content}
    </article>
  </div>

  <footer>
    built with <a href="/">(_x_)</a>
  </footer>
</body>
JOPA
