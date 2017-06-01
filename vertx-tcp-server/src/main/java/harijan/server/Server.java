package harijan.server;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import network.model.Monster;

public class Server extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(Server.class);

	@Override
	public void start() throws Exception {

		logger.debug("Start Server");

		HttpServer httpServer = vertx.createHttpServer();

		httpServer.websocketHandler(new Handler<ServerWebSocket>() {

			@Override
			public void handle(ServerWebSocket socket) {
				
				socket.handler(new Handler<Buffer>() {

					// processing receive data
					@Override
					public void handle(Buffer buffer) {

						ByteBuffer byteBuffer = ByteBuffer.wrap(buffer.getBytes());
						Monster rootAsMonster = Monster.getRootAsMonster(byteBuffer);

						logger.debug("recive data : {}", rootAsMonster.name());
					}
				});

			}
		}).listen(1000);

		super.start();
	}

	@Override
	public void stop() throws Exception {

		logger.debug("Stop Server");

		super.stop();
	}

}