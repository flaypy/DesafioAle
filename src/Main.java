import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Gson gson = new Gson();

        String json = ConsomeAPI.buscarDados();
        List<Post> posts = gson.fromJson(json, new TypeToken<List<Post>>(){}.getType());
//        for (Post post : posts) {
//            System.out.println(post);
//        }

        List<Post> filtragem = posts.stream()
                .filter(post -> post.getTitle().contains("qui"))
                .toList();
//        for (Post post : filtragem) {
//            System.out.println(post);
//        }

        List<Post> ordenacao = filtragem.stream()
                .sorted(Comparator.comparingInt(Post::getUserId))
                .toList();
        System.out.println("Posts filtrados e ordenados: ");

        List<Post> agrupamento = posts.stream()
                .collect(Collectors.groupingBy(Post::getUserId))
                .values()
                .stream()
                .flatMap(List::stream)
                .toList();
//        for (Post post : agrupamento) {
//            System.out.println(post);
//        }

        int ids = filtragem.stream()
                .map(Post::getId)
                .reduce(0, Integer::sum);

        Stream<String> mapeamento = ordenacao.stream()
                .map(Post::getTitle)
                .distinct();

        for (Post post : ordenacao) {
            System.out.println(post);
        }

        System.out.println("total de posts por UserId:");
        Map<Integer, Long> totalPostsPorUserId = posts.stream()
                .collect(Collectors.groupingBy(Post::getUserId, Collectors.counting()));
        for (Map.Entry<Integer, Long> entry : totalPostsPorUserId.entrySet()) {
            System.out.println("UserId: " + entry.getKey() + ", Total de posts: " + entry.getValue());
        }

        System.out.println("Total de ids: " + ids);

        System.out.println("Titulos filtrados e ordenados: ");
        for (String title : mapeamento.toList()) {
            System.out.println("titulo:" + title);
        }
    }
}