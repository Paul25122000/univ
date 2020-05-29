#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 30

typedef struct medicament
{
	char nume[MAX];
	int cantitate;
	struct medicament *urm;
} medicament;

typedef struct reteta
{
	char nume[MAX];
	struct reteta *urm;
	struct medicament *sublista;
} reteta;

typedef struct stoc
{
	char nume[MAX];
	int cantitate;
	int pret;
	struct stoc *urm;
} stoc;

reteta *cautare_reteta(reteta *reteta_cap, char *nume)
{
	reteta *q;
	for (q = reteta_cap; q != NULL && strcmp(q->nume, nume); q = q->urm)
		;
	return q;
}

stoc *cautare_stoc(stoc *depozit_cap, char *nume)
{
	stoc *q;
	for (q = depozit_cap; q != NULL && strcmp(q->nume, nume); q = q->urm)
		;
	return q;
}

stoc *adaugare_stoc(stoc *depozit_cap, char *nume, int cantitate, int pret)
{
	stoc *q1, *q2, *aux;
	aux = (stoc *)malloc(sizeof(stoc));
	strcpy(aux->nume, nume);
	aux->cantitate = cantitate;
	aux->pret = pret;
	aux->urm = NULL;
	for (q1 = q2 = depozit_cap; q1 != NULL && strcmp(q1->nume, aux->nume) < 0; q2 = q1, q1 = q1->urm)
		;
	if (q1 == q2)
	{
		aux->urm = depozit_cap;
		depozit_cap = aux;
	}
	else
	{
		q2->urm = aux;
		aux->urm = q1;
	}
	return depozit_cap;
}

reteta *adaugare_reteta(reteta *reteta_cap, char *nume) //char * med1, int cantitate1, char * med2, int cantitate2, char * med3, int cantitate3
{
	reteta *q1, *q2, *aux;
	aux = (reteta *)malloc(sizeof(reteta));
	strcpy(aux->nume, nume);
	aux->urm = NULL;
	aux->sublista = NULL;
	for (q1 = q2 = reteta_cap; q1 != NULL && strcmp(q1->nume, aux->nume) < 0; q2 = q1, q1 = q1->urm)
		;
	if (q1 == q2)
	{
		aux->urm = reteta_cap;
		reteta_cap = aux;
	}
	else
	{
		q2->urm = aux;
		aux->urm = q1;
	}
	return reteta_cap;
}

medicament *adaugare_med(medicament *med, char *nume_med, int cantitate)
{
	medicament *q1, *q2, *aux;
	aux = (medicament *)malloc(sizeof(medicament));
	strcpy(aux->nume, nume_med);
	aux->cantitate = cantitate;
	aux->urm = NULL;

	for (q1 = q2 = med; q1 != NULL && strcmp(q1->nume, aux->nume) < 0; q2 = q1, q1 = q1->urm)
		;
	if (q1 == q2)
	{
		aux->urm = med;
		med = aux;
	}
	else
	{
		q2->urm = aux;
		aux->urm = q1;
	}
	return med;
}

stoc *citire_stoc(stoc *depozit_cap, char *str)
{
	FILE *f = NULL;
	char nume[MAX];
	int cantitate, pret;
	if ((f = fopen(str, "rt")) == NULL)
	{
		printf("\nEroare la citire_stoca fisierului");
	}
	else
	{
		while (!feof(f))
		{
			fscanf(f, "%s %d %d", nume, &cantitate, &pret);
			depozit_cap = adaugare_stoc(depozit_cap, nume, cantitate, pret);
		}
		fclose(f);
	}

	return depozit_cap;
}

reteta *citire_reteta(reteta *reteta_cap, char *str)
{
	FILE *f = NULL;
	medicament *med = NULL;
	reteta *r = NULL;
	char nume[MAX],
		med1[MAX],
		med2[MAX],
		med3[MAX];
	int cantitate1, cantitate2, cantitate3;
	if ((f = fopen(str, "rt")) == NULL)
	{
		printf("\nEroare la citirea fisierului");
	}
	else
	{
		while (!feof(f))
		{
			fscanf(f, "%s %s %d %s %d %s %d", nume, med1, &cantitate1, med2, &cantitate2, med3, &cantitate3);
			if (cautare_reteta(reteta_cap, nume) == NULL)
				reteta_cap = adaugare_reteta(reteta_cap, nume);
			r = cautare_reteta(reteta_cap, nume);
			med = r->sublista;
			med = adaugare_med(med, med1, cantitate1);
			med = adaugare_med(med, med2, cantitate2);
			med = adaugare_med(med, med3, cantitate3);
			r->sublista = med;
		}
		fclose(f);
	}

	return reteta_cap;
}

void afisare_stoc(stoc *depozit_cap)
{
	stoc *q;
	for (q = depozit_cap; q != NULL; q = q->urm)
	{
		printf("\n%s %2d %2d",
			   q->nume,
			   q->cantitate,
			   q->pret);
	}
}

void afisare_reteta(reteta *reteta_cap)
{
	reteta *r;
	medicament *m;
	for (r = reteta_cap; r != NULL; r = r->urm)
	{
		printf("\n%s", r->nume);
		if (r->sublista != NULL)
			for (m = r->sublista; m != NULL; m = m->urm)
				printf("\n\t%s %2d", m->nume, m->cantitate);
	}
}

void verificare(reteta *reteta_cap, stoc *depozit_cap)
{
	reteta *r = NULL;
	stoc *s = NULL;
	char nume[MAX];
	int suma = 0;
	printf("\nIntroduceti denumirea retetei: ");
	scanf("%s", nume);
	r = cautare_reteta(reteta_cap, nume);
	if (!r)
		printf("\nReteta nu exista\n");
	else
	{
		medicament *m;
		for (m = r->sublista; m != NULL; m = m->urm)
		{
			s = cautare_stoc(depozit_cap, m->nume);
			if (m->cantitate > s->cantitate)
			{
				printf("\ncantitate: %d, %d", m->cantitate, s->cantitate);
				printf("\nNu este suficient %s", m->nume);
				break;
			}
			else
			{
				suma += s->pret * m->cantitate;
			}
		}
	}
	if (suma)
		printf("\nPretul este: %d\n", suma);
}

reteta *reteta_noua(reteta *reteta_cap)
{
	char nume[MAX],
		denumire[MAX],
		c;
	int cantitate;
	reteta *r;
	medicament *m;

	printf("\nIntroduceti denumirea retetei pentru a fi adaugata: ");
	scanf("%s", nume);
	r = cautare_reteta(reteta_cap, nume);

	if (r == NULL)
	{
		reteta_cap = adaugare_reteta(reteta_cap, nume);
	}
	else
	{
		printf("\nAceasta reteta deja exista");
	}

	while (c != 'n')
	{
		printf("\nDoriti sa adaugati medicamente? ('n' - nu): ");
		scanf(" %c", &c);
		if (c == 'n')
			continue;
		r = cautare_reteta(reteta_cap, nume);

		printf("\nIntroduceti denumirea medicamentului: ");
		scanf("%s", denumire);
		printf("\nIntroduceti cantitatea de medicament: ");
		scanf(" %d", &cantitate);

		m = r->sublista;
		m = adaugare_med(m, denumire, cantitate);
		r->sublista = m;
	}

	return reteta_cap;
}

reteta *stergere_reteta(reteta *reteta_cap)
{
	char nume[MAX];
	reteta *q1, *q2;
	printf("\nIntroduceti denumirea retetei pentru a fi stearsa: ");
	scanf("%s", nume);
	for (q1 = q2 = reteta_cap; q1 != NULL && strcmp(q1->nume, nume); q2 = q1, q1 = q1->urm)
		;
	if (q1 != NULL && (strcmp(q1->nume, nume) == 0))
	{
		if (q1 == q2)
		{
			reteta_cap = reteta_cap->urm;
		}
		else
		{
			q2->urm = q1->urm;
			free(q1);
		}
	}
	return reteta_cap;
}
stoc *stergere_med(stoc *depozit_cap)
{
	char nume[MAX];
	stoc *q1, *q2;
	printf("\nIntroduceti denumirea medicamentului pentru a fi sters: ");
	scanf("%s", nume);
	for (q1 = q2 = depozit_cap; q1 != NULL && strcmp(q1->nume, nume); q2 = q1, q1 = q1->urm)
		;
	if (q1 != NULL && (strcmp(q1->nume, nume) == 0))
	{
		if (q1 == q2)
		{
			depozit_cap = depozit_cap->urm;
		}
		else
		{
			q2->urm = q1->urm;
			free(q1);
		}
	}
	return depozit_cap;
}

stoc *medicament_nou(stoc *depozit_cap)
{
	char nume[MAX];
	int cantitate,
		pret;
	printf("\nIntroduceti denumirea medicamentului: ");
	scanf("%s", nume);
	printf("\nIntroduceti cantitatea medicamentului: ");
	scanf("%d", &cantitate);
	printf("\nIntroduceti pretul medicamentului: ");
	scanf("%d", &pret);
	depozit_cap = adaugare_stoc(depozit_cap, nume, cantitate, pret);
	return depozit_cap;
}

stoc *modifica_med(stoc *depozit_cap)
{
	char nume[MAX];
	int cantitate,
		pret;
	stoc *m;
	printf("\nIntroduceti denumirea medicamentului ce dorit sa-l modificati: ");
	scanf("%s", nume);
	m = cautare_stoc(depozit_cap, nume);
	printf("\nIntroduceti cantitatea noua: ");
	scanf("%d", &cantitate);
	m->cantitate = cantitate;
	printf("\nIntroduceti pretul nou: ");
	scanf("%d", &pret);
	m->pret = pret;

	return depozit_cap;
}

void raport_nou(reteta *reteta_cap, stoc *depozit_cap)
{
	FILE *f, *g;
	reteta *r;
	medicament *m;
	stoc *s;
	f = fopen("raport_retete.txt", "wt");
	g = fopen("raport_stoc.txt", "wt");
	if (f == NULL || g == NULL)
		printf("\nEroare la scrierea fisierelor");
	else
	{
		for (r = reteta_cap; r != NULL; r = r->urm) {
			fprintf(f, "%s", r->nume);
			for (m = r->sublista; m != NULL; m = m->urm) {
				fprintf(f, " %s %d", m->nume, m->cantitate);
			}
			fprintf(f, "\n");
		}
		fclose(f);
		for (s = depozit_cap; s != NULL; s = s->urm) {
			fprintf(g, "%s %d %d\n", s->nume, s->cantitate, s->pret);
		}
		fclose(g);
	}
}

void meniu()
{
	int optiune;
	stoc *depozit = NULL;
	reteta *prescriptie = NULL;
	do
	{
		printf("\n1. Incarca datele din fisiere");
		printf("\n2. Afiseaza lista retetelor in ordine alfabetica.");
		printf("\n3. Verifica pentru o reteta citita de la tastatura daca exista toate medicamentele in stoc si care e pretul ei in acest caz.");
		printf("\n4. Afiseaza lista medicamentelor in ordine alfabetica din stoc cu informatiile legate de ele.");
		printf("\n5. Adauga o reteta in lista");
		printf("\n6. Sterge o reteta din lista");
		printf("\n7. Adauga un medicament in lista");
		printf("\n8. Sterge un medicament din lista");
		printf("\n9. Modifica un medicament din lista");
		printf("\n10. Salvare inapoi in fisierele aferente a retetelor si medicamentelor.");
		printf("\n0. Pentru a finisa executia programului");
		printf("\nOptiunea dorita: ");
		scanf("%d", &optiune);
		switch (optiune)
		{
		case 1:
			if (!prescriptie && !depozit)
			{
				prescriptie = citire_reteta(prescriptie, "SDA/Teodora❤️/retete.txt");
				depozit = citire_stoc(depozit, "SDA/Teodora❤️/stoc.txt");
				printf("\nDatele din fisiere au fost încărcate\n");
			}
			else
				printf("\nDatele au fost deja încărcate anterior\n");
			break;
		case 2:
			if (prescriptie)
				afisare_reteta(prescriptie);
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 3:
			if (prescriptie && depozit)
				verificare(prescriptie, depozit);
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 4:
			if (depozit)
				afisare_stoc(depozit);
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 5:
			if (prescriptie)
			{
				prescriptie = reteta_noua(prescriptie);
			}
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 6:
			if (prescriptie)
				prescriptie = stergere_reteta(prescriptie);
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 7:
			if (depozit)
			{
				depozit = medicament_nou(depozit);
			}
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 8:
			if (depozit)
			{
				depozit = stergere_med(depozit);
			}
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 9:
			if (depozit)
			{
				depozit = modifica_med(depozit);
			}
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 10:
			if (prescriptie && depozit)
			{
				raport_nou(prescriptie, depozit);
			}
			else
				printf("\nDatele nu au fost încărcate\n");
			break;
		case 0:
			printf("\n\nO zi buna sa aveti 😋 ");
			break;
		default:
			printf("\nOptiune inexistenta\n");
			break;
		}
	} while (optiune != 0);
}

int main()
{
	meniu();
	return 0;
}