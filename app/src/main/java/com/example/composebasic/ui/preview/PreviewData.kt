package com.example.composebasic.ui.preview

import com.example.composebasic.model.Article
import com.example.composebasic.model.Section
import com.example.composebasic.model.Source

object PreviewData {

    val singleArticle = Article(
        source = Source(id = "bbc-news", name = "BBC News"),
        author = "Jane Smith",
        title = "Global Markets Rally as Inflation Data Comes in Lower Than Expected",
        description = "Stock markets around the world surged on Friday after new data showed inflation cooling faster than analysts had predicted.",
        url = "https://bbc.com/news/business/markets-rally",
        urlToImage = "https://picsum.photos/seed/article1/800/400",
        publishedAt = "2026-06-09T08:30:00Z",
        content = "Global stock markets saw significant gains on Friday following the release of inflation data that beat expectations..."
    )

    val articleList = listOf(
        singleArticle,
        Article(
            source = Source("techcrunch", "TechCrunch"),
            author = "Mark Johnson",
            title = "OpenAI Unveils New Model with Advanced Reasoning Capabilities",
            description = "The AI lab's latest release promises significant improvements in multi-step problem solving and code generation.",
            url = "https://techcrunch.com/2026/06/09/openai-new-model",
            urlToImage = "https://picsum.photos/seed/article2/800/400",
            publishedAt = "2026-06-09T10:15:00Z",
            content = "OpenAI has announced its newest large language model, claiming it outperforms previous versions on a wide range of benchmarks..."
        ),
        Article(
            source = Source("the-verge", "The Verge"),
            author = null,
            title = "Apple's WWDC 2026: Everything Announced",
            description = "From a redesigned operating system to new AI features, here's a full recap of Apple's developer conference.",
            url = "https://theverge.com/2026/6/9/apple-wwdc-recap",
            urlToImage = "https://picsum.photos/seed/article3/800/400",
            publishedAt = "2026-06-09T19:00:00Z",
            content = "Apple kicked off its annual Worldwide Developers Conference with a packed keynote covering iOS, macOS, and more..."
        ),
        Article(
            source = Source(null, "Reuters"),
            author = "Carlos Mendez",
            title = "WHO Declares End to Mpox Public Health Emergency",
            description = "The World Health Organization has officially lifted the global health emergency status for mpox after case numbers declined.",
            url = "https://reuters.com/health/who-mpox-emergency-ends",
            urlToImage = "https://picsum.photos/seed/article4/800/400",
            publishedAt = "2026-06-08T14:45:00Z",
            content = "The World Health Organization announced Tuesday that mpox no longer constitutes a public health emergency of international concern..."
        ),
        Article(
            source = null,
            author = "Priya Nair",
            title = "NASA's Artemis IV Crew Begins Lunar Surface Training",
            description = "The four astronauts set to land on the Moon's south pole are undertaking final simulations in preparation for the 2027 mission.",
            url = "https://nasa.gov/artemis-iv-training",
            urlToImage = "https://picsum.photos/seed/article5/800/400",
            publishedAt = "2026-06-07T11:00:00Z",
            content = null
        )
    )
    val sections get() = listOf(Section("Anger", articleList), Section("Happy", articleList), Section("Sad", articleList))
}