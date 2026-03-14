website_name="JOPA"
website_url="https://neoascetic.github.io/jopa/"
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
  <link rel="stylesheet" href="assets/styles.css">
  <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='-10 0 100 100'><text y='0.85em' font-size='160'>*</text></svg>">
</head>

<body>
  <header>
    <h1><a href="${website_url}">${website_name}</a></h1>
    <h2>${website_description}</h2>
  </header>

  <div class="main">
    <article>
     <h2>${title}</h2>
     ${content}
    </article>
  </div>

  <footer>
    built with <a href="${website_url}">(_*_)</a>
  </footer>

  <link rel="preconnect" href="https://server.ethicalads.io">
  <div id="jopa-ad" class="adaptive" data-ea-publisher="githackcom" data-ea-style="fixedfooter" data-ea-type="text" data-ea-verbosity="quiet"></div>
  <script defer src="https://media.ethicalads.io/media/client/ethicalads.min.js"></script>
</body>
JOPA
