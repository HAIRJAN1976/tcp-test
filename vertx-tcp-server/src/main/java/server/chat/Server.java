package server.chat;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import network.model.Monster;

public class Server extends AbstractVerticle {

	private static Logger logger = LoggerFactory.getLogger(Server.class);

	@Override
	public void start() throws Exception {

		logger.debug("Start Server");

		NetServer netServer = vertx.createNetServer();

		netServer.connectHandler(new Handler<NetSocket>() {

			@Override
			public void handle(NetSocket netSocket) {

				netSocket.handler(new Handler<Buffer>() {

					@Override
					public void handle(Buffer buffer) {
						
						ByteBuffer bb = ByteBuffer.wrap(buffer.getBytes());
						Monster rootAsMonster = Monster.getRootAsMonster(bb);
						
						logger.debug("recive data : {}", rootAsMonster.name());

						Buffer outBuffer = Buffer.buffer();
						outBuffer.appendString("response ...");

						netSocket.write(outBuffer);
					}
				});
			}

		});

		netServer.listen(1000);

		super.start();
	}

	@Override
	public void stop() throws Exception {

		logger.debug("Stop Server");

		super.stop();
	}

}