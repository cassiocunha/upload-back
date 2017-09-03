/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monkeyhand.upload.resources;

import br.com.monkeyhand.upload.models.Product;
import br.com.monkeyhand.upload.models.exception.BussinesException;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.util.StreamUtils;

/**
 *
 * @author cassio
 */
@Path("/upload")
public class UploadResource {

    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @POST
    public Response uploadFile(MultipartFormDataInput input) throws IOException, BussinesException {
        saveFile(input);
        String json = input.getFormDataPart("product", new GenericType<String>() {});
        Gson gson = new Gson();
        Product product = gson.fromJson(json, Product.class);
        System.out.println(product);
        return Response.status(200).build();
    }

    private void saveFile(MultipartFormDataInput input) throws IOException {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("image");
        if (!inputParts.isEmpty()) {
            InputPart inputPart = inputParts.get(0);
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            String fileName = "/home/cassio/Downloads/" + getFileName(header);
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            writeFile(bytes, fileName);
        }
    }

    //Obtiene el nombre del archivo que está en el header de la petición
    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }
}
