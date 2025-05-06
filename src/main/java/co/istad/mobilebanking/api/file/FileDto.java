package co.istad.mobilebanking.api.file;

import lombok.Builder;

@Builder
public record FileDto(

        String name,

        String downloadUrl,

        String url,

        String extension,

        long size

) {
}
