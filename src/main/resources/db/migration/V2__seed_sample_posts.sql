INSERT INTO posts (
    id, slug, language, translation_group_id, title, excerpt,
    status, visibility, content_ref, published_at, created_at, updated_at
)
VALUES
(
    gen_random_uuid(),
    'intro-zh',
    'zh-cn',
    '11111111-1111-1111-1111-111111111111',
    '日本IT求职路线图入门',
    '这是给在日中国留学生准备的 IT 求职入门文章。',
    'published',
    'free',
    'posts/intro-zh.md',
    now(),
    now(),
    now()
),
(
    gen_random_uuid(),
    'intro-ja',
    'ja-jp',
    '11111111-1111-1111-1111-111111111111',
    '日本IT就職ロードマップ入門',
    '在日留学生向けの IT 就職入門記事です。',
    'published',
    'free',
    'posts/intro-ja.md',
    now(),
    now(),
    now()
);