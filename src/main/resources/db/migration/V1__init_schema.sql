CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    email varchar(255) NOT NULL,
    password_hash varchar(255) NOT NULL,
    email_verified boolean NOT NULL DEFAULT false,
    enabled boolean NOT NULL DEFAULT false,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE TABLE profiles (
    user_id uuid PRIMARY KEY,
    nickname varchar(100),
    age_range varchar(20),
    occupation varchar(50),
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT profiles_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT profiles_age_range_check CHECK (age_range IN ('18-20', '21-23', '24-26', '27+')),
    CONSTRAINT profiles_occupation_check CHECK (occupation IN ('student', 'employee', 'part-time', 'other'))
);

CREATE TABLE roles (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(50) NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT roles_name_unique UNIQUE (name)
);

CREATE TABLE user_roles (
    user_id uuid NOT NULL,
    role_id uuid NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_roles_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT user_roles_role_fk FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE
);

CREATE TABLE posts (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    slug varchar(255) NOT NULL,
    language varchar(20) NOT NULL,
    translation_group_id uuid NOT NULL,
    title varchar(255) NOT NULL,
    excerpt text,
    status varchar(20) NOT NULL,
    visibility varchar(20) NOT NULL,
    content_ref varchar(500) NOT NULL,
    published_at timestamp with time zone,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT posts_slug_language_unique UNIQUE (slug, language),
    CONSTRAINT posts_translation_group_language_unique UNIQUE (translation_group_id, language),
    CONSTRAINT posts_status_check CHECK (status IN ('draft', 'published')),
    CONSTRAINT posts_visibility_check CHECK (visibility IN ('free')),
    CONSTRAINT posts_language_check CHECK (language IN ('zh-cn', 'ja-jp'))
);

CREATE TABLE email_verification_tokens (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    token varchar(255) NOT NULL,
    expires_at timestamp with time zone NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    CONSTRAINT email_verification_tokens_user_fk FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT email_verification_tokens_token_unique UNIQUE (token)
);

CREATE UNIQUE INDEX idx_roles_name ON roles(name);
CREATE UNIQUE INDEX idx_posts_slug_language ON posts(slug, language);
CREATE UNIQUE INDEX idx_posts_translation_group_language ON posts(translation_group_id, language);
CREATE INDEX idx_posts_language_status_published_at ON posts(language, status, published_at DESC);
CREATE INDEX idx_posts_translation_group_id ON posts(translation_group_id);
CREATE INDEX idx_posts_slug ON posts(slug);
CREATE INDEX idx_posts_updated_at ON posts(updated_at DESC);
CREATE UNIQUE INDEX idx_email_verification_tokens_token ON email_verification_tokens(token);
CREATE INDEX idx_email_verification_tokens_user_id ON email_verification_tokens(user_id);
CREATE INDEX idx_email_verification_tokens_expires_at ON email_verification_tokens(expires_at);

INSERT INTO roles (name)
VALUES ('USER'), ('ADMIN')
ON CONFLICT (name) DO NOTHING;