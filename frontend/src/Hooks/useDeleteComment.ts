import { deleteComment } from "../Services/services";
import { useMutation, useQueryClient } from "@tanstack/react-query";

export const useDeleteComment = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (commentId: number) => deleteComment(commentId),
    onSuccess: (data) => {
      console.log("успешно");

      console.log(data);

      queryClient.invalidateQueries({ queryKey: ["taskBody"] });
    },
    onError: (error) => console.log(error.name),
  });
};
