export default function Head() {
  return (
    <>
      {/* Basic Meta Tags */}
      <title>subsero | Simplify Subscription Management</title>
      <meta
        name="description"
        content="Take control of your subscriptions with subsero. Track renewals, save money, and avoid hidden charges effortlessly. Manage all your subscriptions in one secure platform."
      />
      <meta name="keywords" content="subscription management, track subscriptions, save money, subscription tracker, renewal alerts" />
      <meta name="author" content="subsero" />

      {/* Open Graph Meta Tags */}
      <meta property="og:title" content="subsero | Simplify Subscription Management" />
      <meta
        property="og:description"
        content="Track, manage, and optimize your subscriptions with subsero. Save money and stay on top of renewals effortlessly."
      />
      <meta property="og:image" content="https://www.subsero.com/images/og-image.jpg" />
      <meta property="og:url" content="https://www.subsero.com/" />
      <meta property="og:type" content="website" />

      {/* Twitter Meta Tags */}
      <meta name="twitter:card" content="summary_large_image" />
      <meta name="twitter:title" content="subsero | Simplify Subscription Management" />
      <meta
        name="twitter:description"
        content="Track, manage, and optimize your subscriptions with subsero. Save money and stay on top of renewals effortlessly."
      />
      <meta name="twitter:image" content="https://www.subsero.com/images/twitter-image.jpg" />

      <link rel="icon" href="/images/favicon/favicon.ico?v=2" />
      <script
        type="application/ld+json"
        dangerouslySetInnerHTML={{
          __html: JSON.stringify({
            "@context": "https://schema.org",
            "@type": "WebPage",
            "name": "subsero | Simplify Subscription Management",
            "description": "Take control of your subscriptions with subsero. Track renewals, save money, and avoid hidden charges effortlessly. Manage all your subscriptions in one secure platform.",
            "url": "https://www.subsero.com/",
            "image": "https://www.subsero.com/images/og-image.jpg",
            "author": {
              "@type": "Organization",
              "name": "subsero"
            },
            "publisher": {
              "@type": "Organization",
              "name": "subsero",
              "logo": {
                "@type": "ImageObject",
                "url": "https://www.subsero.com/images/logo.png"
              }
            }
          }),
        }}
      />

    </>
  );
}