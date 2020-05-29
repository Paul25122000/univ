#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 30
typedef struct subnod
{
    int vechime;
    char nume[MAX],
        prenume[MAX];
    struct subnod *urm;
} subnod;

typedef struct nod
{
    char departament[MAX];
    struct nod *urm;
    struct subnod *sublista;
} nod;

nod *adaugareNod(nod *capLista, char *departament);
subnod *adaugareSubnod(subnod *capLista, char *nume, char *prenume, int vechime);
nod *citireNod(nod *capLista);
nod *cauta(nod *capLista, char *departament);
void afisare(nod *capLista);
void cautaDep(nod *capLista);
void exportare(nod *capLista);


int main()
{
    nod *lista = NULL;
    subnod *sublista = NULL;
    lista = citireNod(lista);
    afisare(lista);
    cautaDep(lista);
    exportare(lista);
    return 0;
}

nod *adaugareNod(nod *capLista, char *departament)
{
    nod *aux,
        *q1, *q2;
    aux = (nod *)malloc(sizeof(nod));
    strcpy(aux->departament, departament);
    aux->urm = NULL;
    aux->sublista = NULL;
    for (q1 = q2 = capLista; q1 != NULL && strcmp(q1->departament, aux->departament) < 0; q2 = q1, q1 = q1->urm)
        ;
    if (q1 == q2)
    {
        aux->urm = capLista;
        capLista = aux;
    }
    else
    {
        q2->urm = aux;
        aux->urm = q1;
    }
    return capLista;
}
subnod *adaugareSubnod(subnod *capLista, char *nume, char *prenume, int vechime)
{
    subnod *aux,
        *q1, *q2;
    aux = (subnod *)malloc(sizeof(subnod));
    strcpy(aux->nume, nume);
    strcpy(aux->prenume, prenume);
    aux->vechime = vechime;
    aux->urm = NULL;
    for (q1 = q2 = capLista; q1 != NULL && strcmp(q1->nume, aux->nume) < 0; q2 = q1, q1 = q1->urm)
        ;
    if (q1 == q2)
    {
        aux->urm = capLista;
        capLista = aux;
    }
    else
    {
        q2->urm = aux;
        aux->urm = q1;
    }
    return capLista;
}

nod *cauta(nod *capLista, char *departament)
{
    nod *lista;
    for (lista = capLista; lista != NULL; lista = lista->urm)
    {
        if (strcmp(lista->departament, departament) == 0)
            return lista;
    }
    return NULL;
}

nod *citireNod(nod *capLista)
{
    FILE *f = NULL;
    int vechime;
    char nume[MAX],
        prenume[MAX],
        departament[MAX];
    subnod *sublista;

    if ((f = fopen("SDA/Teodora❤️/Examen/intrare.txt", "rt")) == NULL)
        printf("\nEroare la deschiderea fisierului");
    else
    {
        while (!feof(f))
        {
            fscanf(f, "%s %s %d %s", nume, prenume, &vechime, departament);
            if (cauta(capLista, departament) == NULL)
            {
                capLista = adaugareNod(capLista, departament);
                sublista = cauta(capLista, departament)->sublista;
                sublista = adaugareSubnod(sublista, nume, prenume, vechime);
                cauta(capLista, departament)->sublista = sublista;
            }
            else
            {
                sublista = cauta(capLista, departament)->sublista;
                sublista = adaugareSubnod(sublista, nume, prenume, vechime);
                cauta(capLista, departament)->sublista = sublista;
            }
        }
        fclose(f);
    }
    return capLista;
}

void afisare(nod *capLista)
{
    nod *lista;
    subnod *sublista;
    for (lista = capLista; lista != NULL; lista = lista->urm)
    {
        printf("\n%s", lista->departament);
        if (lista->sublista != NULL)
            for (sublista = lista->sublista; sublista != NULL; sublista = sublista->urm)
                printf("\n\t%s %s %d", sublista->nume, sublista->prenume, sublista->vechime);
    }
}

void cautaDep(nod *capLista)
{
    char departament[MAX];
    nod *lista;
    subnod *sublista;
    printf("\nIntroduceti denumirea departamentului: ");
    scanf("%s", departament);
    if ((lista = cauta(capLista, departament)) != NULL)
    {
        if (lista->sublista != NULL)
            for (sublista = lista->sublista; sublista != NULL; sublista = sublista->urm)
                printf("\n\t%s %s %d", sublista->nume, sublista->prenume, sublista->vechime);
    }
    else
    {
        printf("\nDepartamentul \"%s\" nu exista", departament);
    }
}

void exportare(nod *capLista)
{
    FILE *f = NULL;
    f = fopen("SDA/Teodora❤️/Examen/iesire.txt", "wt");
    int vechime;
    printf("\nIntroduceti vechimea minima: ");
    scanf("%d", &vechime);
    nod *lista;
    subnod *sublista;
    for (lista = capLista; lista != NULL; lista = lista->urm)
    {
        if (lista->sublista != NULL)
            for (sublista = lista->sublista; sublista != NULL; sublista = sublista->urm)
                if(sublista->vechime >= vechime)
                    fprintf(f, "%s %s %d %s\n", sublista->nume, sublista->prenume, sublista->vechime, lista->departament);
    }
}