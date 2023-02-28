package me.kleidukos.object.tables;

import me.kleidukos.util.Languages;
import me.kleidukos.util.MetaTags;

public record TranslationResult(Languages lang, MetaTags tag, String val) { }
