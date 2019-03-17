package ua.in.devmind.dxfPlot.generation;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import ua.in.devmind.dxfPlot.model.data.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

public class DxfGenerator {

    public static void generateDxf(File file, List<Point> pointsList) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(properties);
        VelocityContext context = new VelocityContext();
        context.put("pointsList", pointsList);
        Template template = null;
        try {
            template = Velocity.getTemplate("/templates/dxftemplate.vm");
        } catch (Exception e) {
            // unable to load template
            e.printStackTrace();
        }
        try (FileWriter fw = new FileWriter(file)) {
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            fw.write(sw.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
