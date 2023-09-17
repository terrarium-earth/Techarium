package earth.terrarium.techarium.common.config;

import com.teamresourceful.resourcefulconfig.common.annotations.Config;
import com.teamresourceful.resourcefulconfig.web.annotations.Gradient;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;

@Config("techarium")
@WebInfo(
    title = "Techarium",
    description = "",

    icon = "cog",
    gradient = @Gradient(value = "45deg", first = "#7F4DEE", second = "#E7797A")
)
public final class TechariumConfig {

}
