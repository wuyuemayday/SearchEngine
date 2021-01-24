package mapper;

import entity.coordinator.CoordinateResponse;
import entity.document.DocumentScore;
import entity.proto.SearchModel;

import java.util.ArrayList;
import java.util.List;

public final class CoordinatorMapper {

    public static SearchModel.SearchResponse CoordinateResponseToProtobufResponse(final CoordinateResponse in) {
        final SearchModel.SearchResponse.Builder out = SearchModel.SearchResponse.newBuilder();
        final List<SearchModel.SearchResponse.DocumentStats> documents = new ArrayList<>();
        for (final DocumentScore ds : in.getResult()) {
            final SearchModel.SearchResponse.DocumentStats stats = SearchModel.SearchResponse.DocumentStats.newBuilder()
                    .setName(ds.getDocName())
                    .setScore(ds.getScore())
                    .build();
            documents.add(stats);
        }

        return out.addAllDocuments(documents).build();
    }
}
