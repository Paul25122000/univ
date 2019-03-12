#include <stdio.h>

int binarySearch(int a[], int item, int low, int high) {
    if (high <= low)
        return (item > a[low]) ? (low + 1) : low;

    int mid = (low + high) / 2;

    if (item == a[mid])
        return mid + 1;

    if (item > a[mid])
        return binarySearch(a, item, mid + 1, high);
    return binarySearch(a, item, low, mid - 1);
}

void insertionSort(int a[], int n) {
    int i, loc, j, k, selected;

    for (i = 1; i < n; ++i) {
        j = i - 1;
        selected = a[i];

        loc = binarySearch(a, selected, 0, j);

        while (j >= loc) {
            a[j + 1] = a[j];
            j--;
        }
        a[j + 1] = selected;
    }
}

void showOff(int arr[], int n) {
    int i;
    for (i = 0; i < n; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

int main() {
    int arr[] = {3 ,0 ,2 ,1 ,5 ,6},
        n = sizeof(arr) / sizeof(arr[0]);

    printf("\nInitial array: "); showOff(arr, n);
    insertionSort(arr, n);

    printf("\nSorted array: "); showOff(arr, n);
    return 0;
}