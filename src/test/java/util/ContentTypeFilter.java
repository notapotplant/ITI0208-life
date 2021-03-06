package util;

import javax.annotation.Priority;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Priority(0)
public class ContentTypeFilter implements ClientResponseFilter {

    @Override
    public void filter(ClientRequestContext request,
                       ClientResponseContext response) throws IOException {

        String accept = getFirst(request.getHeaders().get("Accept"));

        if (!accept.contains("json")) {
            return;
        }
        System.out.println(response);
        System.out.println(response.getMediaType());

        String contentType = getFirst(response.getHeaders().get("Content-Type"));

        if (contentType.isEmpty()) {
            throw new RuntimeException("Content-Type is missing");
        } else if (!contentType.contains("json")) {
            throw new RuntimeException("Unexpected Content-Type: " + contentType);
        }
    }

    private String getFirst(List<? super String> list) {
        return Optional.ofNullable(list)
                .map(List::stream)
                .orElseGet(Stream::empty)
                .map(String::valueOf)
                .findFirst()
                .orElse("");
    }
}
