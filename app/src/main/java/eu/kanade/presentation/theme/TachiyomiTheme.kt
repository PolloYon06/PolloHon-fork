package eu.kanade.presentation.theme

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import eu.kanade.domain.ui.model.AppTheme
import eu.kanade.presentation.theme.colorscheme.BaseColorScheme
import eu.kanade.presentation.theme.colorscheme.CatppuccinColorScheme
import eu.kanade.presentation.theme.colorscheme.GreenAppleColorScheme
import eu.kanade.presentation.theme.colorscheme.LavenderColorScheme
import eu.kanade.presentation.theme.colorscheme.MidnightDuskColorScheme
import eu.kanade.presentation.theme.colorscheme.MonetColorScheme
import eu.kanade.presentation.theme.colorscheme.MonochromeColorScheme
import eu.kanade.presentation.theme.colorscheme.NordColorScheme
import eu.kanade.presentation.theme.colorscheme.RetroColorScheme
import eu.kanade.presentation.theme.colorscheme.StrawberryColorScheme
import eu.kanade.presentation.theme.colorscheme.TachiyomiColorScheme
import eu.kanade.presentation.theme.colorscheme.TakoColorScheme
import eu.kanade.presentation.theme.colorscheme.TealTurqoiseColorScheme
import eu.kanade.presentation.theme.colorscheme.TidalWaveColorScheme
import eu.kanade.presentation.theme.colorscheme.TokyoNightColorScheme
import eu.kanade.presentation.theme.colorscheme.YinYangColorScheme
import eu.kanade.presentation.theme.colorscheme.YotsubaColorScheme
import mihon.app.di.appGraph
import tachiyomi.presentation.core.util.collectAsState

@Composable
fun TachiyomiTheme(
    appTheme: AppTheme? = null,
    amoled: Boolean? = null,
    classicWeb: Boolean? = null,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val uiPreferences = remember { context.appGraph.uiPreferences }
    val storedAppTheme by uiPreferences.appTheme.collectAsState()
    val storedAmoled by uiPreferences.themeDarkAmoled.collectAsState()
    val storedClassicWeb by uiPreferences.classicWebMode.collectAsState()
    BaseTachiyomiTheme(
        appTheme = appTheme ?: storedAppTheme,
        isAmoled = amoled ?: storedAmoled,
        isClassicWeb = classicWeb ?: storedClassicWeb,
        content = content,
    )
}

@Composable
fun TachiyomiPreviewTheme(
    appTheme: AppTheme = AppTheme.DEFAULT,
    isAmoled: Boolean = false,
    content: @Composable () -> Unit,
) = BaseTachiyomiTheme(appTheme, isAmoled, isClassicWeb = false, content = content)

@Composable
private fun BaseTachiyomiTheme(
    appTheme: AppTheme,
    isAmoled: Boolean,
    isClassicWeb: Boolean,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val scheme = remember(appTheme, isDark, isAmoled, isClassicWeb) {
        if (isClassicWeb) {
            RetroColorScheme.getColorScheme(isDark, isAmoled = false, overrideDarkSurfaceContainers = false)
        } else {
            getThemeColorScheme(
                context = context,
                appTheme = appTheme,
                isDark = isDark,
                isAmoled = isAmoled,
            )
        }
    }

    MaterialExpressiveTheme(
        colorScheme = scheme,
        typography = remember(isClassicWeb) { if (isClassicWeb) classicWebTypography else Typography() },
        shapes = remember(isClassicWeb) { if (isClassicWeb) classicWebShapes else Shapes() },
    ) {
        if (!isClassicWeb) {
            content()
            return@MaterialExpressiveTheme
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scheme.background)
                .classicWebFrame(scheme = scheme, isDark = isDark),
        ) {
            content()
        }
    }
}

/**
 * Monospace headings and labels for the classic web mode, while long-form text keeps the
 * default font family so descriptions and chapter titles stay comfortable to read.
 */
private val classicWebTypography: Typography = Typography().let { base ->
    val mono = FontFamily.Monospace
    base.copy(
        displayLarge = base.displayLarge.copy(fontFamily = mono),
        displayMedium = base.displayMedium.copy(fontFamily = mono),
        displaySmall = base.displaySmall.copy(fontFamily = mono),
        headlineLarge = base.headlineLarge.copy(fontFamily = mono),
        headlineMedium = base.headlineMedium.copy(fontFamily = mono),
        headlineSmall = base.headlineSmall.copy(fontFamily = mono),
        titleLarge = base.titleLarge.copy(fontFamily = mono),
        titleMedium = base.titleMedium.copy(fontFamily = mono),
        titleSmall = base.titleSmall.copy(fontFamily = mono),
        labelLarge = base.labelLarge.copy(fontFamily = mono),
        labelMedium = base.labelMedium.copy(fontFamily = mono),
        labelSmall = base.labelSmall.copy(fontFamily = mono),
    )
}

/**
 * Sharp, window-like corners instead of the rounded modern shapes.
 */
private val classicWebShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(2.dp),
    large = RoundedCornerShape(2.dp),
    extraLarge = RoundedCornerShape(2.dp),
)

/**
 * Draws faint CRT-style scanlines and an inset frame over the whole UI, evoking an old browser
 * window. Purely decorative: it never handles pointer input, so every interaction passes through.
 */
private fun Modifier.classicWebFrame(scheme: ColorScheme, isDark: Boolean): Modifier = drawWithContent {
    drawContent()

    val scanlineColor = if (isDark) Color.White.copy(alpha = 0.02f) else Color.Black.copy(alpha = 0.025f)
    val lineHeight = 1.dp.toPx()
    val lineSpacing = 3.dp.toPx()
    var y = 0f
    while (y < size.height) {
        drawRect(
            color = scanlineColor,
            topLeft = Offset(0f, y),
            size = Size(size.width, lineHeight),
        )
        y += lineSpacing
    }

    val frameWidth = 1.dp.toPx()
    drawRect(
        color = scheme.outline.copy(alpha = 0.45f),
        topLeft = Offset(frameWidth / 2, frameWidth / 2),
        size = Size(size.width - frameWidth, size.height - frameWidth),
        style = Stroke(width = frameWidth),
    )
}

private fun getThemeColorScheme(
    context: Context,
    appTheme: AppTheme,
    isDark: Boolean,
    isAmoled: Boolean,
): ColorScheme {
    val colorScheme = if (appTheme == AppTheme.MONET) {
        MonetColorScheme(context)
    } else {
        colorSchemes.getOrDefault(appTheme, TachiyomiColorScheme)
    }
    return colorScheme.getColorScheme(
        isDark = isDark,
        isAmoled = isAmoled,
        overrideDarkSurfaceContainers = appTheme != AppTheme.MONET,
    )
}

private val colorSchemes: Map<AppTheme, BaseColorScheme> = mapOf(
    AppTheme.DEFAULT to TachiyomiColorScheme,
    AppTheme.CATPPUCCIN to CatppuccinColorScheme,
    AppTheme.TOKYONIGHT to TokyoNightColorScheme,
    AppTheme.GREEN_APPLE to GreenAppleColorScheme,
    AppTheme.LAVENDER to LavenderColorScheme,
    AppTheme.MIDNIGHT_DUSK to MidnightDuskColorScheme,
    AppTheme.MONOCHROME to MonochromeColorScheme,
    AppTheme.NORD to NordColorScheme,
    AppTheme.STRAWBERRY_DAIQUIRI to StrawberryColorScheme,
    AppTheme.TAKO to TakoColorScheme,
    AppTheme.TEALTURQUOISE to TealTurqoiseColorScheme,
    AppTheme.TIDAL_WAVE to TidalWaveColorScheme,
    AppTheme.YINYANG to YinYangColorScheme,
    AppTheme.YOTSUBA to YotsubaColorScheme,
)
