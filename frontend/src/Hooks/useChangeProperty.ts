import { useMutation, useQueryClient } from "@tanstack/react-query";
import {
  deleteProperty,
  updateProperty,
  addProperty,
  Action,
  IChangeProperty,
} from "../Services/services";
import { useErrorPropStore } from "../store";

export const useChangeProperty = (action: Action) => {
  const queryClient = useQueryClient();
  const { setError } = useErrorPropStore();

  switch (action) {
    case "add":
      return useMutation({
        mutationFn: (data: IChangeProperty) => addProperty(data),
        onSuccess: (_, variables) => {
          console.log(variables);

          queryClient.invalidateQueries({
            queryKey: [`${variables.property}`],
          });
        },
        onError: (error) => console.log(error),
      });
    case "delete":
      return useMutation({
        mutationFn: (data: IChangeProperty) => deleteProperty(data),
        onSuccess: (_, variables) => {
          setError(false);
          queryClient.invalidateQueries({
            queryKey: [`${variables.property}`],
          });
        },

        onError: () => setError(true),
      });
    case "update":
      return useMutation({
        mutationFn: (data: IChangeProperty) => updateProperty(data),
        onSuccess: (_, variables) =>
          queryClient.invalidateQueries({
            queryKey: [`${variables.property}`],
          }),
      });
  }
};
