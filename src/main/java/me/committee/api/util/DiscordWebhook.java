package me.committee.api.util;

import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.util.*;

public class DiscordWebhook {

    private final String url;
    private final String username;
    private final String avatarURL;

    public DiscordWebhook(String url) {
        this.url = url;
        this.username = null;
        this.avatarURL = null;
    }
    public DiscordWebhook(String url, String username) {
        this.url = url;
        this.username = username;
        this.avatarURL = null;
    }

    public DiscordWebhook(String url, String username, String avatarURL) {
        this.url = url;
        this.username = username;
        this.avatarURL = avatarURL;
    }

    private void postWebhook(String json) {
        try {
            final HttpClient httpClient = HttpClientBuilder.create().build();
            final HttpPost request = new HttpPost(this.url);

            request.addHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(json));

            httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> generateJson() {
        final Map<String, Object> data = new HashMap<>();
        if (this.username != null) data.put("username", this.username);
        if (this.avatarURL != null) data.put("avatar_url", this.avatarURL);
        return data;
    }

    public void postMessage(String message, List<DiscordEmbed> discordEmbed) {
        final Map<String, Object> data = this.generateJson();
        data.put("embeds", discordEmbed);
        data.put("content", message);

        postWebhook(new Gson().toJson(data));
    }

    public void postMessage(String message, DiscordEmbed discordEmbed) {
        final Map<String, Object> data = this.generateJson();
        data.put("embeds", Collections.singletonList(discordEmbed));
        data.put("content", message);

        postWebhook(new Gson().toJson(data));
    }

    public void postMessage(String message) {
        final Map<String, Object> data = this.generateJson();
        data.put("content", message);

        postWebhook(new Gson().toJson(data));
    }

    public void postMessage(DiscordEmbed discordEmbed) {
        final Map<String, Object> data = this.generateJson();
        data.put("embeds", Collections.singletonList(discordEmbed));

        postWebhook(new Gson().toJson(data));
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public static class DiscordEmbed {

        private final String title;
        private final Author author;
        private final String description;
        private final int color;
        private final List<Field> fields;

        public DiscordEmbed(String title, Author author, int color, List<Field> fields) {
            this.title = title;
            this.author = author;
            this.color = color;
            this.fields = fields;
            this.description = null;
        }

        public DiscordEmbed(String title, Author author, int color, Field field) {
            this.title = title;
            this.author = author;
            this.color = color;
            this.fields = Collections.singletonList(field);;
            this.description = null;
        }

        public DiscordEmbed(Author author, int color, List<Field> fields) {
            this.author = author;
            this.color = color;
            this.fields = fields;
            this.title = null;
            this.description = null;
        }

        public DiscordEmbed(List<Field> fields) {
            this.fields = fields;
            this.author = null;
            this.color = -1;
            this.title = null;
            this.description = null;
        }

        public DiscordEmbed(String title, String description, Author author, int color, List<Field> fields) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.color = color;
            this.fields = fields;
        }

        public DiscordEmbed(String title, String description, List<Field> fields) {
            this.title = title;
            this.description = description;
            this.fields = fields;
            this.author = null;
            this.color = -1;
        }

        public DiscordEmbed(String title, String description, Author author, int color, Field field) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.color = color;
            this.fields = Collections.singletonList(field);
        }

        public DiscordEmbed(String title, String description, Field field) {
            this.title = title;
            this.description = description;
            this.fields = Collections.singletonList(field);
            this.author = null;
            this.color = -1;
        }

        public DiscordEmbed(String title, String description, Author author, int color) {
            this.title = title;
            this.description = description;
            this.author = author;
            this.color = color;
            this.fields = new ArrayList<>();
        }

        public DiscordEmbed(String title, String description) {
            this.title = title;
            this.description = description;
            this.author = null;
            this.color = -1;
            this.fields = new ArrayList<>();
        }

        public void addField(Field field) {
            this.fields.add(field);
        }

        public void delField(Field field) {
            this.fields.remove(field);
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Author getAuthor() {
            return author;
        }

        public int getColor() {
            return color;
        }

        public List<Field> getFields() {return fields;}

        public static class Author {

            private final String name;
            private final String icon_url;

            public Author(String name, String icon_url) {
                this.name = name;
                this.icon_url = icon_url;
            }

            public Author(String name) {
                this.name = name;
                this.icon_url = null;
            }

            public String getName() {
                return name;
            }
            public String getIconUrl() {
                return icon_url;
            }
        }

        public static class Field {

            private final String name;
            private final String value;
            private final Boolean inline;

            public Field(String name, String value, Boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            public Field(String name, String value) {
                this.name = name;
                this.value = value;
                this.inline = false;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }

            public Boolean getInline() {
                return inline;
            }
        }

    }

}
