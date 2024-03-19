import { likeComment } from "../Services/services";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ILikeComment } from "../Services/services";

export const useLike = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: ILikeComment) => likeComment(data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["taskBody"] }),
  });
};
