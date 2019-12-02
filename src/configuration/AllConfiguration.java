package configuration;

import core.netserver.ServerConfig;
import game.message.handler.HandlerConfig;
import game.metadata.MetaConfiguration;
import game.playerino.PlayerInfoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;

@Configuration
@Import({game.user.UserConfiguration.class, HandlerConfig.class, ServerConfig.class, PlayerInfoConfiguration.class, MetaConfiguration.class})
public class AllConfiguration {
}
