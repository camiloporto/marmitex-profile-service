package br.com.camiloporto.marmitex.microservice.profile.util;

import br.com.camiloporto.marmitex.microservice.profile.model.Profile;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ur42 on 17/03/2016.
 */
public class BulkDeleteRequest {

    @Getter
    private List<Map<Object, Object>> docs;

    public BulkDeleteRequest(List<Profile> profilesToDelete) {
        docs = new ArrayList<>();
        for (Profile p: profilesToDelete) {
            Map<Object, Object> doc = new HashMap<>();
            doc.put("_id", p.getId());
            doc.put("_rev", p.getRevision());
            doc.put("_deleted", true);
            docs.add(doc);
        }
    }
}
