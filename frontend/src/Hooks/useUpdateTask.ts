import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateTask } from "../Services/services";
import { ITaskDataUpdate } from "../Services/services";
const useUpdateTask = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (data: ITaskDataUpdate) => updateTask(data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["tasks"] }),
    onError: (error) => console.log(error),
  });
};
export default useUpdateTask;
