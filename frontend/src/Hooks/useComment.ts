import { sendComment } from "../Services/services";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { IComment } from "../Services/services";

export const useComment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: IComment) => sendComment(data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["taskBody"] }),
  });
};
